package tv.notube.platform;

import com.sun.jersey.api.core.InjectParam;
import tv.notube.analytics.Analyzer;
import tv.notube.analytics.AnalyzerException;
import tv.notube.analytics.analysis.AnalysisDescription;
import tv.notube.analytics.analysis.AnalysisResult;
import tv.notube.analytics.analysis.MethodDescription;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.lang.Object;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/analytics")
@Produces(MediaType.APPLICATION_JSON)
public class AnalyticsService {

    @InjectParam
    private InstanceManager instanceManager;

    @GET
    @Path("/analysis")
    public Response getAvailableAnalysis() {
        Analyzer analyzer = instanceManager.getAnalyzer();
        AnalysisDescription[] analysisDescriptions;
        try {
            analysisDescriptions = analyzer.getRegisteredAnalysis();
        } catch (AnalyzerException e) {
            throw new RuntimeException(
                    "Error while getting registered analysis",
                    e
            );
        }
        return new Response(
                Response.Status.OK,
                "analysis found",
                Arrays.asList(analysisDescriptions)
        );
    }

    @GET
    @Path("/analysis/{name}")
    public Response getAnalysisDescription( @PathParam("name") String name ) {
        Analyzer analyzer = instanceManager.getAnalyzer();
        AnalysisDescription analysisDescription;
        try {
            analysisDescription = analyzer.getAnalysisDescription(name);
        } catch (AnalyzerException e) {
            throw new RuntimeException(
                    "Error while getting registered analysis",
                    e
            );
        }
        return new Response(
                Response.Status.OK,
                "analysis description",
                analysisDescription
        );
    }

    @GET
    @Path("/analysis/{name}/{user}/{method}")
    public Response getAnalysisResult(
            @PathParam("name") String name,
            @PathParam("user") String user,
            @PathParam("method") String methodName,
            @Context UriInfo uriInfo
    ) {
        Analyzer analyzer = instanceManager.getAnalyzer();
        AnalysisDescription analysisDescription;
        try {
            analysisDescription = analyzer.getAnalysisDescription(name);
        } catch (AnalyzerException e) {
            throw new RuntimeException(
                    "Error while getting analysis description",
                    e
            );
        }
        AnalysisResult analysisResult;
        try {
            analysisResult = analyzer.getResult(name, user);
        } catch (AnalyzerException e) {
            throw new RuntimeException(
                    "Error while getting registered analysis",
                    e
            );
        }
        if(analysisResult == null) {
            return new Response(
                    Response.Status.NOK,
                    "analysis without result"
            );
        }
        String resultClassName = analysisDescription.getResultClassName();
        MethodDescription mds[] =
                analysisDescription.getMethodDescriptions(methodName);

        String params[] = getParams(uriInfo.getQueryParameters().get("param"));

        Object result = getResult(
                analysisResult,
                resultClassName,
                mds,
                params
        );
        return new Response(Response.Status.OK, "analysis result", result);
    }

    private String[] getParams(List<String> values) {
        if(values == null)
            return new String[0];
        return values.toArray(new String[values.size()]);
    }

    private Object getResult(
            AnalysisResult analysisResult,
            String resultClassName,
            MethodDescription mds[],
            String params[]
    ) {
        Class analysisResultClass;
        try {
            analysisResultClass = Class.forName(resultClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Error while getting registered analysis",
                    e
            );
        }
        Object typedResult = analysisResultClass.cast(analysisResult);
        return invokeMethod(typedResult, mds, params);
    }

    private Object invokeMethod(
            Object typedResult,
            MethodDescription[] mds,
            String[] params
    ) {
        Method method;
        for (MethodDescription md : mds) {
            if (params.length == md.getParameterTypes().length) {
                Class<? extends Object> methodSignature[] =
                        getMethodSignature(md.getParameterTypes());
                try {
                    method = typedResult.getClass().getMethod(
                            md.getName(),
                            methodSignature
                    );
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Method '" + md.getName() + "'",
                            e);
                }
                try {
                    return method.invoke(
                            typedResult,
                            getActualSignature(md.getParameterTypes(), params)
                    );
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("method not found");
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("method not found");
                }
            }
        }
        throw new RuntimeException("method not found");
    }

    private Object[] getActualSignature(
            String[] parameterTypes,
            String[] params
    ) {
        Object objs[] = new Object[params.length];
        Class<? extends Object>[] signature = getMethodSignature(parameterTypes);
        int i = 0;
        for(Class<? extends Object> c : signature) {
            try {
                objs[i] = c.getConstructor(String.class).newInstance(params[i]);
            } catch (InstantiationException e) {
                throw new RuntimeException("" ,e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("" ,e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("" ,e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("" ,e);
            }
            i++;
        }
        return objs;
    }

    private Class<? extends Object>[] getMethodSignature(
            String[] parameterTypes) {
        List<Class<? extends Object>> classes = new ArrayList<Class<? extends Object>>();
        for(String parameterType : parameterTypes) {
            try {
                classes.add(Class.forName(parameterType));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(
                        "Parameter with type: '" + parameterType + "'",
                        e
                );
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }

}
