<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
      <meta charset="utf-8">
      <title>Floriparide admin</title>
      <base href="${pageContext.request.contextPath}">
      <style type="text/css">
          #loading-mask{
              background-color:white;
              height:100%;
              position:absolute;
              left:0;
              top:0;
              width:100%;
              z-index:2000;
          }
          #loading{
              height:auto;
              position:absolute;
              left:45%;
              top:40%;
              padding:2px;
              width: 200px;
              z-index:20001;
          }
          #loading a {
              color:#225588;
          }
          #loading .loading-indicator{
              background:white;
              color:#444;
              font:bold 13px Helvetica, Arial, sans-serif;
              height:auto;
              margin:0;
              padding:10px;
          }
          #loading-msg {
              font-size: 10px;
              font-weight: normal;
          }
      </style>
  </head>
  <body class="x-border-box">
      <div id="loading-mask" style=""></div>
      <div id="loading">
          <div class="loading-indicator">
              <img src="/images/loading.gif" width="32" height="32" style="margin-right:8px;float:left;vertical-align:top;"/>Floriparide admin
              <br /><span id="loading-msg">Loading styles and images...</span>
          </div>
      </div>
      <script src="/app/config.js" type="text/javascript"></script>
      <script src="/app/bootstrap.js" type="text/javascript"></script>

  </body>
</html>
