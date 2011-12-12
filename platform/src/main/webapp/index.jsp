<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NoTube 2.0 Beancounter Platform REST API Documentation</title>
    </head>
    <body>
        <h1>NoTube Beancounter 2.0 Platform REST API Documentation </h1>

        <h2>Overview</h2>
        <p>
        This page describes the new Beancounter REST APIs that will quicky replace
        those described <a href="https://notube.sti2.org/wiki/index.php/Asemantics_Beancounter_REST">here</a>.
        </p>
        <p>
        The API root URL is http://moth.notube.tv:9090/notube-platform/rest/
        <strong>[note: 9090 port is subjected to change in the future]</strong>. All the subsequent
        methods are to be intended as relative to that URL. Responses are available
        in JSON format only and are all in this format:</p>

<p>
        <pre><code>
        {
            "status": OK | NOK ,
            "message": <i>a success of error message</i>,
            "object": <i>an optional object that may be returned</i>
        }
        </code></pre>
        </p>
        <p>
        <strong>DISCLAIMER: responses are going to change frequently. Be aware and ofter refer to this page</strong>
        </p>

        <h3>1) Register a new user</h3>

        Path: /user/register <br>
        Method: POST         <br>
        Parameters:            <br>
            - username        <br>
            - password         <br>
            - name               <br>
            - surname          <br>
        Description: it registers a new user to the Beancounter with username, password, name and surname.<br>
        Example: with curl<br> <br>

        curl -d "name=Davide&surname=Palmisano&username=dpalmisano&password=abracadabra" http://moth.notube.tv:9090/notube-platform/rest/user/register   <br>
        with parameter body (application/x-www-form-urlencoded). <br>

        response:
        <pre><code>
        {
            "status":"OK",
            "message":"user successfully registered",
            "object":"5fe903d3-c6ef-49ba-a9af-44d91f028138"
        }
        </code></pre>

        The value in the 'object' fields is the unique user identifier in the
        Beancounte plaform. Could be considered equivalent to the user name. <br>
        If the username is already taken, the service replies: <br>

        <pre><code>
        {
            "status":"NOK",
            "message":"username 'dpalmisano' is already taken"
        }
        </code></pre>

        <h3>2) Get user data</h3>

        Path: /user/{username}<br>
        Method: GET<br>
        Parameters: {username} the Beancounter username<br>
        Description: it returns the user data but not the profile.<br>
        Example:<br><br>

        GET http://moth.notube.tv:9090/notube-platform/rest/user/dpalmisano<br><br>

        response:
        <pre><code>
        {
            "status":"OK",
            "message":"user 'dpalmisano' found",
            "object":
                {
                    "name":"Davide",
                    "surname":"Palmisano",
                    "forcedProfiling":false,
                    "activities":[],
                    "services":{},
                    "username":"dpalmisano",
                    "id":"5fe903d3-c6ef-49ba-a9af-44d91f028138"
                }
        }
        </code></pre>

        <h3>3) Get user activities</h3>

        Path: /user/activities{username}<br>
        Method: GET<br>
        Parameters: {username} the Beancounter username<br>
        Description: it returns all the user activites on the sources he's
        subscribed to.<br>
        Example:<br><br>

        GET http://moth.notube.tv:9090/notube-platform/rest/user/activities/dpalmisano

        <h3>4) Delete a user</h3>

        Path: /user/{username}<br>
        Method: DELETE<br>
        Parameters: {username} the Beancounter username<br>
        Description: it deletes all the user data and activities.<br>
        Example: with curl<br><br>

        curl -X DELETE http://moth.notube.tv:9090/notube-platform/rest/user/merlin

        <h3>5) Authenticate a user</h3>

        Path: /user/authenticate/{username}<br>
        Method: POST<br>
        Parameters:<br>
        - {username} the Beancounter username<br>
        - {password} the user password
        Description: it authenticates or not a user.<br>
        Example: with curl<br><br>

        curl -d "password=abracadabra" http://moth.notube.tv:9090/notube-platform/rest/user/authenticate/dpalmisano

        <h3>6) Get a user profile</h3>

        Path: /user/profile/{username}<br>
        Method: GET<br>
        Parameters: {username} the Beancounter username<br>
        Description: it returns a weighted interests profile in JSON.<br>
        Example: with curl<br><br>

        curl http://moth.notube.tv:9090/notube-platform/rest/user/profile/dpalmisano

        <h3>7) Add a source (lastfm, gomiso.com, twitter, facebook,
        n-screen) to a user</h3>

        NoTube 2.0 Beancounter APIs support only those services which are
        OAuth-compliant or OAuth-like compliant. Twitter (OAuth-compliant)
        and Last.fm (OAuth-like) examples follow:<br>

        <h4> Twitter </h4>
        1) Send your Beancounter user with {username} to this url:<br>
        http://moth.notube.tv:9090/notube-platform/rest/user/oauth/token/twitter/{username}<br>
        2) Then, the user will be redirected to his Twitter account home
        page to authorize the Beancounter application.

        <h4> Facebook </h4>
        1) Send your Beancounter user with {username} to this url:<br>
        http://moth.notube.tv:9090/notube-platform/rest/user/oauth/token/facebook/dpalmisano<br>
        2) Then, the user will be redirected to his Facebook account home
        page to authorize the Beancounter application.

        <h4> Last.fm </h4>
        It's a bit different since it uses an OAuth-like exchange mechanism.<br>
        1)  Send your user here: http://www.last.fm/api/auth/?api_key=9f57b916d7ab90a7bf562b9e6f2385f0&cb=http://moth.notube.tv:9090/notube-platform/rest/user/callback/lastfm/{username}   <br>
        where {username} is the Beancounter username<br>
        2) then your user will be asked to grant permission to NoTube on his last.fm profile page. Once done it will be redirect to a page confirming the authorization process with this response:    <br><br>
        <pre><code>
        {
            "status":"OK",
            "message":"service 'lastfm' as been successfully added to user 'dpalmisano'"
        }
        </code></pre>
                                                                                                                       <br>
        you can easily verify calling:<br>

        GET http://moth.notube.tv:9090/notube-platform/rest/user/dpalmisano<br>

        to see 'lastfm' or 'twitter' as a service added on the user services list.

        <h3>8) Remove a source (lastfm, gomiso.com, twitter, facebook,
        n-screen) to a user</h3>

        Path: /user/source/{username}/{service}<br>
        Method: DELETE<br>
        Parameters:
        - {username} the Beancounter username<br>
        - {service} the service you want to remove from the user
        Description: it deletes a service from a user. That service will be
        not used anymore until you don't add it again.
        <br>
        Example: with curl<br><br>

        curl -X DELETE http://moth.notube.tv:9090/notube-platform/rest/user/source/merlin/lastfm

        <h3>Analytics APIs</h3>

        The Beancounter foresees a set APIs to access statistical data about
        activities and interests of a user. Just to introduce some
        terminology and definition.<br><br>

        1) an <b>analysis</b> is a set of statistical data grouped under an
        identifying name and with a timestamp of its latest execution. <br>

        2) each <b>analysis</b> could be accessed using some <b>method</b>s
        it declares. A <b>method</b> has an identifying name and could
        eventually accept parameters. See examples of invocation below.<br><br>

        <h4>Get available analysis</h4>
        Path: /analytics/analysis<br>
        Method: GET<br>
        Parameters: none<br>
        Description: it returns all the user analysis the Beancounter is able
        to perform.<br>
        Example: http://moth.notube.tv:9090/notube-platform/rest/analytics/analysis<br><br>


        <h4>Get a description of a specific analysis</h4>
        Path: /analytics/analysis/{name}<br>
        Method: GET<br>
        Parameters: {name} the name identifiyng the analysis<br>
        Description: it returns a description of the specified analysis.<br>
        Example: http://moth.notube.tv:9090/notube-platform/rest/analytics/analysis/timeframe-analysis<br><br>


        <h4>Calling a method of an analysis for a specific user</h4>
        Path: /analytics/analysis/{name}/{user}/{method}?param={param}<br>
        Method: GET<br>
        Parameters: <br>
        - {name} the name identifiyng the analysis<br>
        - {user} the user id on which the analysis has been performed<br>
        - {method} the analysis specific <b>method</b> <br>
        - {param} the method parameter, could be omitted for some methods<br>
        Description: it returns some statistics as speficied by the analysis
        and its method descriptions.<br>
        Example: http://moth.notube.tv:9090/notube-platform/rest/analytics/analysis/timeframe-analysis/8c33b0e6-d3cf-4909-b04c-df93056e64a8/getStatistics?param=4<br><br>

        Let's go through and example.<br>
        1) Firstly, retrieve some information on the
        <i>timeframe-analysis</i>:<br>
        curl http://moth.notube.tv:9090/notube-platform/rest/analytics/analysis/timeframe-analysis <br>

        as you can see from the response:<br>
        <pre>
        <code>
        {
            "status": "OK",
            "message": "analysis description",
            "object": {
                "name": "timeframe-analysis",
                "description": "this analysis summarizes the user activities over time",
                "methodDescriptions": [
                    {
                        "name": "getStatistics",
                        "description": "this method returns activity statistics day by day of the last month"
                    }
                ]
            }
        }
</code>
</pre>
        the analysis foresees a <i>getStatistics</i> method which returns
        some statistics about a day of the last month. If you want to know
        how many tweets, songs listened or shares the user did on the 4th
        day of the last month you can simply call:<br><br>

        http://moth.notube.tv:9090/notube-platform/rest/analytics/analysis/timeframe-analysis/8c33b0e6-d3cf-4909-b04c-df93056e64a8/getStatistics?param=4<br>

        <br>
        obtaining a result looking like:<br>

    <pre><code>
        {
            "status": "OK",
            "message": "analysis result",
            "object": {
                "activities": {
                    "TWEET": 8,
                    "LISTEN": 10
                },
            "services": {
                "http://twitter.com": 8,
                "http://last.fm": 10
            },
            "executedAt": "2011-12-06T16:48:30.287Z"
        }
    }
</code>
</pre>
        which means that on that day, the user performed that number of activities, grouped by services and verb.
    </body>
</html>
