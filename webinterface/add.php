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
                <li><a href="./add.php">Submit an Event</a></li>
              </ul>
            </div>
          </div>
        </div>

      </div>
    </div>


  <div class = "container">
    <div class="row">
      <div class="col-lg-12">
        <center>
  Add an event to the calendar<br/><br/>
  <form action="added.php" method="POST">
    Name of Event: <input type="text" name="name" /><br/><br/>
    Building Name:
      <select name="building">
<?php
  $curl = curl_init();
  curl_setopt($curl, CURLOPT_URL,"http://wesley-crusher.firba1.com:8080/api/v1.0/location/getlocations");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
  $json = curl_exec($curl);
  curl_close($curl); 
  $jsonobj = json_decode($json);
  $locations = $jsonobj->locations;
        $i = 1;
        $locationArray = array();
  foreach($locations as $location){
  print "<option value=\"".$location."\">".$location."</option>";
} ?>
    </select><br/><br/>
    Start Date: &nbsp;
    <select name="month">
    <option value="1">January</option><option value="2">February</option>
    <option value="3">March</option><option value="4">April</option>
    <option value="5">May</option><option value="6">June</option>
    <option value="7">July</option><option value="8">August</option>
    <option value="9">September</option><option value="10">October</option>
    <option value="11">November</option><option value="12">December</option>
    </select>
<select name="day">
<?php
$z = 1;
for($z=1;$z<32;$z++){
print "<option value=\"".$z."\">".$z."</option>";}
?>
</select>
<br/><br/>
Start Time: &nbsp;
<select name="starttime">
<option value="09:00:00">9:00am</option><option value="09:30:00">9:30am</option>
<option value="10:00:00">10:00am</option><option value="10:30:00">10:30am</option>
<option value="11:00:00">11:00am</option><option value="11:30:00">11:30am</option>
<option value="12:00:00">12:00pm</option><option value="12:30:00">12:30pm</option>
<option value="13:00:00">1:00pm</option><option value="13:30:00">1:30pm</option>
<option value="14:00:00">2:00pm</option><option value="14:30:00">2:30pm</option>
<option value="15:00:00">3:00pm</option><option value="15:30:00">3:30pm</option>
<option value="16:00:00">4:00pm</option><option value="16:30:00">4:30pm</option>
<option value="17:00:00">5:00pm</option><option value="17:30:00">5:30pm</option>
<option value="18:00:00">6:00pm</option><option value="18:30:00">6:30pm</option>
<option value="19:00:00">7:00pm</option><option value="19:30:00">7:30pm</option>
</select><br/><br/>
End Time:&nbsp;
<select name="endtime">
<option value="09:00:00">9:00am</option><option value="09:30:00">9:30am</option>
<option value="10:00:00">10:00am</option><option value="10:30:00">10:30am</option>
<option value="11:00:00">11:00am</option><option value="11:30:00">11:30am</option>
<option value="12:00:00">12:00pm</option><option value="12:30:00">12:30pm</option>
<option value="13:00:00">1:00pm</option><option value="13:30:00">1:30pm</option>
<option value="14:00:00">2:00pm</option><option value="14:30:00">2:30pm</option>
<option value="15:00:00">3:00pm</option><option value="15:30:00">3:30pm</option>
<option value="16:00:00">4:00pm</option><option value="16:30:00">4:30pm</option>
<option value="17:00:00">5:00pm</option><option value="17:30:00">5:30pm</option>
<option value="18:00:00">6:00pm</option><option value="18:30:00">6:30pm</option>
<option value="19:00:00">7:00pm</option><option value="19:30:00">7:30pm</option>
</select>
  <br/><input type="submit" value="Add event"/>
  </form>
</center>
      </div>
  </div>

<!--List of Upcoming Events by Time Frame-->

  <div class="container">
    
    <div class="row">
      
    </div>
  </div>



    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="./assets/js/jquery.js"></script>
    <script src="./dist/js/bootstrap.js"></script>
    <script src="./assets/js/holder.js"></script>
  </body>
</html>
