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
        {"status": OK | NOK ,"message": <i>a success of error message</i>, "object": <i>an optional object that may be returned</i>}
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
        Description: it registers a new user to the Beancounter with username, password, name and surname.      <br>
        Example:                      <br> <br>

        POST http://moth.notube.tv:9090/notube-platform/rest/user/register   <br>
        with parameter body (application/x-www-form-urlencoded):                        <br>
        name=Davide&surname=Palmisano&username=dpalmisano&password=abracadabra<br>      <br>

        response: {"status":"OK","message":"user successfully registered","object":"5fe903d3-c6ef-49ba-a9af-44d91f028138"}
                                                                                                                                                <br> <br>
        if the username is already taken, the service replies:                                                                     <br>  <br>
                                                                                                                                                                    <br>
        {"status":"NOK","message":"username 'dpalmisano' is already taken"}                                                           <br>

        <h3>2) Get user data</h3>

        Path: /user/{username}         <br>
        Method: GET                                 <br>
        Parameters: none                           <br>
        Description: it returns the user data but not the profile. <br>
        Example:                                                                             <br> <br>

        GET http://moth.notube.tv:9090/notube-platform/rest/user/dpalmisano  <br> <br>

        response: {"status":"OK","message":"user 'dpalmisano' found","object":{"name":"Davide","surname":"Palmisano","forcedProfiling":false,"activities":[],"services":{},"username":"dpalmisano","id":"5fe903d3-c6ef-49ba-a9af-44d91f028138"}}
                                                                                                                             <br>  <br>
        <h3>2) Add a source (lastfm, 4sq, twitter, facebook, n-screen) to the user</h3>

        It's a bit different since it uses the OAuth exchange mechanism. The following example works for Lastfm.      <br>
        1)  Send your user here: http://www.last.fm/api/auth/?api_key=9f57b916d7ab90a7bf562b9e6f2385f0&cb=http://moth.notube.tv:9090/notube-platform/rest/user/callback/lastfm/{username}   <br>
        where {username} is the Beancounter username   <br>
        2) then your user will be asked to grant permission to NoTube on his last.fm profile page. Once done it will be redirect to a page confirming the authorization process with this response:    <br><br>
        {"status":"OK","message":"service 'lastfm' as been successfully added to user 'dpalmisano'"}
                                                                                                                       <br>
        you can easily verify calling:                       <br>

        GET http://moth.notube.tv:9090/notube-platform/rest/user/dpalmisano        <br>

        to see 'lastfm' as a service added on the user services list.






    </body>
</html>
