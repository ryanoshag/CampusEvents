<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="../../assets/ico/favicon.png">

    <script type="text/javascript"
          src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false">
     </script>

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
                <li><a href="./add.php">Submit an Event</a></li>
              </ul>
            </div>
          </div>
        </div>

      </div>
    </div>

<!--Google Map with Markers-->
  <div class = "container">
    <div class="row">
      <div class="col-lg-6">
          <?php 
            $currentLocation = htmlspecialchars($_GET['location']);
            print "<h3>Upcoming Events at ". $currentLocation . "</h3>"; ?>
      </div>
    </div>
    <div class="row">
      <div class="col-lg-6">
        <!--<h2>Today</h2>-->
        <ul class="nav nav-pills nav-stacked">

          <li>-</li>

          <?php
            $curl = curl_init();
            curl_setopt($curl, CURLOPT_URL,"http://wesley-crusher.firba1.com:8080/api/v1.0/location/geteventbylocation/" . rawurlencode($currentLocation));
            curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
            $json = curl_exec($curl);
            curl_close($curl); 
            $jsonobj = json_decode($json);
            $locations = $jsonobj->locations;
            $jsonobj->events = array_slice($jsonobj->events,0,5);
            if($jsonobj->events) {
              foreach($jsonobj->events as $event){
                print "<li><strong>Event: </strong>" . $event->event_name . "</li>";
                print "<li><strong>Date: </strong>" . $event->start_date . " from " . $event->start_time . " to " .$event->end_time . "</li>";
                print "<li>-</li>";
              } 
            }
            else {
              print "<li>No upcoming events at " . $currentLocation . "</li>";
            }
          ?>

          

          <?php
            /*$curl = curl_init();
            curl_setopt($curl, CURLOPT_URL,"http://wesley-crusher.firba1.com:8080/api/v1.0/location/geteventsfornexthours/72");//geteventbylocation/" . rawurlencode($currentLocation));
            curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
            $json = curl_exec($curl);
            curl_close($curl); 
            $jsonobj = json_decode($json);
            $jsonobj->events = array_slice($jsonobj->events,0,5);

            foreach($jsonobj->events as $event) {
              $eventobj = json_encode($event);
              print "<li><strong>Event: </strong>" . $event->event_name . "</li>";
              print "<li><strong>Date: </strong>" . $event->start_date . " at " . $event->start_time . "</li>";
              //print "</a></li>";
            }*/
          ?>

        </ul>
      </div>
    </div>
  </div>
        <!--Google Map with Markers-->
        <script type="text/javascript">
          function initialize() {
            <?php
              $curl = curl_init();
              curl_setopt($curl, CURLOPT_URL,"http://wesley-crusher.firba1.com:8080/api/v1.0/location/nametocoordinates/" . rawurlencode($currentLocation));
              curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
              $json = curl_exec($curl);
              curl_close($curl); 
              $jsonobj = json_decode($json);
              $latitude = $jsonobj->latitude;
              $longitude = $jsonobj->longitude;
              ?>
            var mapOptions = {
              scrollwheel: false,
              center: new google.maps.LatLng(<?php echo json_encode($latitude); ?>, <?php echo json_encode($longitude); ?>),
              zoom: 15,
              mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            var map = new google.maps.Map(document.getElementById("map-canvas1"),
                mapOptions);

            var crc = new google.maps.Marker({
              position: new google.maps.LatLng(<?php echo json_encode($latitude); ?>, <?php echo json_encode($longitude); ?>),
              map: map,
              title:<?php echo json_encode($currentLocation); ?>
            });
          }
          google.maps.event.addDomListener(window, 'load', initialize);
        </script>

        <div class="google-map-canvas" id="map-canvas1"> </div>

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


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="./assets/js/jquery.js"></script>
    <script src="./dist/js/bootstrap.js"></script>
    <script src="./assets/js/holder.js"></script>
  </body>
</html>
