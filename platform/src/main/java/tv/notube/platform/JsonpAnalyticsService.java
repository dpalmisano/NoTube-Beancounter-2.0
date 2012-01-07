package tv.notube.platform;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.json.JSONWithPadding;
import tv.notube.analytics.Analyzer;
import tv.notube.analytics.AnalyzerException;
import tv.notube.analytics.analysis.AnalysisDescription;
import tv.notube.analytics.analysis.AnalysisResult;
import tv.notube.analytics.analysis.MethodDescription;
import tv.notube.applications.ApplicationsManager;
import tv.notube.applications.ApplicationsManagerException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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
@Path("/jsonp/analytics")
@Produces("application/x-javascript")
public class JsonpAnalyticsService extends JsonpService {

    @InjectParam
    private InstanceManager instanceManager;

    @GET
    @Path("/analysis")
    public JSONWithPadding getAvailableAnalysis(
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback
    ) {
        try {
            check(
                    this.getClass(),
                    "getAvailableAnalysis",
                    apiKey,
                    callback
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters", callback);
        }
        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authorizing your application", callback);
        }
        if (!isAuth) {
            return new JSONWithPadding(new JsonpPlatformResponse(
                    JsonpPlatformResponse.Status.NOK,
                    "Your application is not authorized.Sorry."), callback
            );
        }

        Analyzer analyzer = instanceManager.getAnalyzer();
        AnalysisDescription[] analysisDescriptions;
        try {
            analysisDescriptions = analyzer.getRegisteredAnalysis();
        } catch (AnalyzerException e) {
            return error(e, "Error while getting registered analysis", callback);
        }
        return new JSONWithPadding(new JsonpPlatformResponse(
                JsonpPlatformResponse.Status.OK,
                "analysis found",
                Arrays.asList(analysisDescriptions)), callback
        );
    }

    @GET
    @Path("/analysis/{name}")
    public JSONWithPadding getAnalysisDescription(
            @PathParam("name") String name,
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback
    ) {
        try {
            check(
                    this.getClass(),
                    "getAnalysisDescription",
                    name,
                    apiKey,
                    callback
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters", callback);
        }
        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authorizing your application", callback);
        }
        if (!isAuth) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "Your application is not authorized.Sorry."
                    ),
                    callback
            );
        }
        Analyzer analyzer = instanceManager.getAnalyzer();
        AnalysisDescription analysisDescription;
        try {
            analysisDescription = analyzer.getAnalysisDescription(name);
        } catch (AnalyzerException e) {
            return error(e, "Error while getting registered analysis", callback);
        }
        return new JSONWithPadding(
                new JsonpPlatformResponse(
                        JsonpPlatformResponse.Status.OK,
                        "analysis description",
                        analysisDescription),
                callback
        );
    }

    @GET
    @Path("/analysis/{name}/{user}/{method}")
    public JSONWithPadding getAnalysisResult(
            @PathParam("name") String name,
            @PathParam("user") String user,
            @PathParam("method") String methodName,
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback,
            @Context UriInfo uriInfo
    ) {
        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authorizing your application", callback);
        }
        if (!isAuth) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "Your application is not authorized.Sorry."),
                    callback
            );
        }
        Analyzer analyzer = instanceManager.getAnalyzer();
        AnalysisDescription analysisDescription;
        try {
            analysisDescription = analyzer.getAnalysisDescription(name);
        } catch (AnalyzerException e) {
            return error(e, "Error while getting analysis description", callback);
        }
        AnalysisResult analysisResult;
        try {
            analysisResult = analyzer.getResult(name, user);
        } catch (AnalyzerException e) {
            return error(e, "Error while getting registered analysis", callback);
        }
        if (analysisResult == null) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "analysis without result"),
                    callback
            );
        }
        String resultClassName = analysisDescription.getResultClassName();
        MethodDescription mds[] =
                analysisDescription.getMethodDescriptions(methodName);

        String params[] = getParams(uriInfo.getQueryParameters().get("param"));

        Object result;
        try {
            result = getResult(
                    analysisResult,
                    resultClassName,
                    mds,
                    params
            );
        } catch (ClassNotFoundException e) {
            return error(e, "Class not found", callback);
        } catch (NoSuchMethodException e) {
            return error(e, "Method not found", callback);
        } catch (IllegalAccessException e) {
            return error(e, "Error while accessing method", callback);
        } catch (InvocationTargetException e) {
            return error(e, "Error while invocating method", callback);
        } catch (InstantiationException e) {
            return error(e, "Error while instantiating result", callback);
        }
        return new JSONWithPadding(
                new JsonpPlatformResponse(JsonpPlatformResponse.Status.OK,
                        "analysis result", result),
                callback
        );
    }

    private String[] getParams(List<String> values) {
        if (values == null)
            return new String[0];
        return values.toArray(new String[values.size()]);
    }

    private Object getResult(
            AnalysisResult analysisResult,
            String resultClassName,
            MethodDescription mds[],
            String params[]
    ) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        Class analysisResultClass;
        analysisResultClass = Class.forName(resultClassName);
        Object typedResult = analysisResultClass.cast(analysisResult);
        return invokeMethod(typedResult, mds, params);
    }

    private Object invokeMethod(
            Object typedResult,
            MethodDescription[] mds,
            String[] params
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Method method;
        for (MethodDescription md : mds) {
            if (params.length == md.getParameterTypes().length) {
                Class<? extends Object> methodSignature[] =
                        getMethodSignature(md.getParameterTypes());
                    method = typedResult.getClass().getMethod(
                            md.getName(),
                            methodSignature
                    );
                    return method.invoke(
                            typedResult,
                            getActualSignature(md.getParameterTypes(), params)
                    );

            }
        }
        throw new NoSuchMethodException("method not found");
    }

    private Object[] getActualSignature(
            String[] parameterTypes,
            String[] params
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object objs[] = new Object[params.length];
        Class<? extends Object>[] signature = getMethodSignature(parameterTypes);
        int i = 0;
        for (Class<? extends Object> c : signature) {

                objs[i] = c.getConstructor(String.class).newInstance(params[i]);
            i++;
        }
        return objs;
    }

    private Class<? extends Object>[] getMethodSignature(
            String[] parameterTypes) throws ClassNotFoundException {
        List<Class<? extends Object>> classes = new ArrayList<Class<? extends Object>>();
        for (String parameterType : parameterTypes) {
            classes.add(Class.forName(parameterType));
        }
        return classes.toArray(new Class[classes.size()]);
    }

}
