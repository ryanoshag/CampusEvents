<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="../../assets/ico/favicon.png">

    <title>GT Campus Events</title>

    <!-- Bootstrap core CSS -->
    <link href="./dist/css/bootstrap.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="../../assets/js/html5shiv.js"></script>
      <script src="../../assets/js/respond.min.js"></script>
    <![endif]-->

    <!-- Custom styles for this template -->
    <link href="carousel.css" rel="stylesheet">

  </head>
<!-- NAVBAR
================================================== -->
  <body>
    <div class="navbar-wrapper">
      <div class="container">

        <div class="navbar navbar-inverse navbar-static-top">
          <div class="container">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="./index.php">GT Campus Events</a>
            </div>
            <div class="navbar-collapse collapse">
              <ul class="nav navbar-nav">
                <li class="active"><a href="./index.php">Home</a></li>
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">Locations<b class="caret"></b></a>
                  <ul class="dropdown-menu">
                    <?php
                    $curl = curl_init();
                    curl_setopt($curl, CURLOPT_URL,"http://wesley-crusher.firba1.com:8080/api/v1.0/location/getlocations");
                    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
                    $json = curl_exec($curl);
                    curl_close($curl); 
                    $jsonobj = json_decode($json);
                    $locations = $jsonobj->locations;
                    foreach($locations as $location){
                      print "<li><a href=\"./locations.php?location=$location\">".$location."</a></li>";
                    }
                    ?>
                  </ul>
                </li>
                <li><a href="./index.php#events">Upcoming Events</a></li>
              </ul>
            </div>
          </div>
        </div>

      </div>
    </div>

<!--Google Map with Markers-->
  <div class = "container">
    <div class="row">
      <div class="col-lg-12">
        <a name="events">
          <?php print "<h1>Add an event"; ?>
        </a>
      </div>
    </div>
    <div class="row">
      <div class="col-lg-6">
        <!--<h2>Today</h2>-->
        <ul class="nav nav-pills nav-stacked">

          <li><strong>Event</strong>: Hypnotist</li>
          <li><strong>Date</strong>: 11/6/2013 from 1 AM - 3 AM</li>
          <li><strong>Description</strong>: Come out and be mesmerized by the illusions of a professional hynotist!</li>
          <li>-</li>
          <li><strong>Event</strong>: GT Dining Student Appreciation Night</li>
          <li><strong>Date</strong>: 11/7/2013 from 6 PM - 8 PM</li>
          <li><strong>Description</strong>: Come enjoy your favorite Student Center restaurants as they celebrate their favorite customers - you!</li>
        </ul>
      </div>
      <div class="col-lg-12">
        <div class="google-map-canvas" id="map-canvas1"> </div>
      </div>
  </div>

<!--List of Upcoming Events by Time Frame-->

  <div class="container">
    
    <div class="row">
      
    </div>
  </div>

      <!-- FOOTER -->
      <footer>
        <p class="pull-right"><a href="#">Back to top</a></p>
        <p>&copy; 2013 TEAM INFINITY EXPLOSION MAXIMUM ULTRA</p>
      </footer>

    </div><!-- /.container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="./assets/js/jquery.js"></script>
    <script src="./dist/js/bootstrap.js"></script>
    <script src="./assets/js/holder.js"></script>
  </body>
</html>
