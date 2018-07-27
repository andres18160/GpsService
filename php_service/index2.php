<?php
include "config.php";
include "utils.php";
$dbConn =  connect($db);
?>
<!DOCTYPE HTML>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <title>Prueba canvas</title>
    </head>
        <body>	
            <section>

                <div>
                    <canvas id="myCanvas" width="1000" height="1000" style="border:1px solid #000000;"></canvas>
                </div>
                <div id="coordenadas"></div>
            </section>
            <?php
             $sql = "SELECT * FROM `beacons_detail` WHERE IdHead=(SELECT MAX(Id) FROM beacons WHERE IdPhone='3079c6c4bd209faf')";
             $statement = $dbConn->prepare($sql);
             $statement->execute();
           $response=$statement -> fetchAll();             
           echo "<pre>";
           print_r($response);

            ?>
        </body>
</html>
<script>

    var beacons=[  
        {            
            id:"0eae3df4-102f-444f-8007-d712ffe83724",//ROSA
            num:4,
            x:100,
            y:0,
            distance:""
        },
        {            
            id:"1d13b961-752e-41c3-9f0f-53ec8f623cac",//ROJO
            num:6,
            x:350,
            y:600,
            distance:""
        },
        {            
            id:"acc0eeff-9287-46bd-9d79-8c3f103e6684",//AMARILLO
            num:5,
            x:600,
            y:0,
            distance:""
        }
       
        
        
    ];

    var ctx = document.getElementById('myCanvas').getContext('2d');
    var imgAnt = new Image();
    imgAnt.src = 'https://static1.squarespace.com/static/594100ad03596ed295e03804/5943e9e2e110ebe1d7add3c7/5943e9e73e00be46daff4977/1497623021587/Beacon.png';
    var imgDispo = new Image();
    imgDispo.src = 'https://www.shareicon.net/data/256x256/2015/09/25/107071_internet_512x512.png';
    var x=0;
    var y=0;






    function dibujarDispositivo()
    {       
        <?php
             $sql = "SELECT * FROM `beacons_detail` WHERE IdHead=(SELECT MAX(Id) FROM beacons WHERE IdPhone='3079c6c4bd209faf')";
             $statement = $dbConn->prepare($sql);
             $statement->execute();
           $response=$statement -> fetchAll();             
          // echo "<pre>";
         //  print_r($response);

            ?>
        var dispositivos=JSON.parse('<?php echo json_encode($response); ?>');

            ctx.clearRect(0, 0, 1000, 1000);

           // ctx.drawImage(imgFondo, 0,0,500,500);
            $.each( beacons, function( key, value ) {            
               ctx.drawImage(imgAnt, value.x, value.y,30,30);
            });      
           
            ctx.beginPath();
            $.each(dispositivos,function(ikey,i){
                $.each( beacons, function( key, value ) {     
                    if(i.Beacon==value.id){                       
                        i.x=value.x;
                        i.y=value.y;                        
                        ctx.arc(i.x,i.y,i.distance*100,0,2*Math.PI,false);                        
                    }
                }); 
            });  
            ctx.stroke();
           
           
            var res= trilateracion(dispositivos[0],dispositivos[1],dispositivos[2]);

            console.log("Se ejecuto x="+res.x+" y="+res.y);
            $("#coordenadas").html("Se ejecuto x="+res.x+" y="+res.y);
            ctx.drawImage(imgDispo, res.x, res.y,30,30);
            setTimeout("location.reload()", 2000);
    }


      function trilateracion(a1,a2,a3){
          debugger;
          var r1=a1.distance;
          var r2=a2.distance;
          var r3=a3.distance;

          var d=a2.x-a1.x;
          var i=a3.x-a1.x;
          var j=a3.y-a1.y;
          var x = (r1**2 - r2**2 + d**2)/(2*d);
          var y = (r1**2 - r3**2 - x**2 + (x-i)**2 + j**2)/(2*j);
          x+=a1.x;
          y+=a1.y;
          var cor={
              x:x,
              y:y
          }
          return cor;
      }

    dibujarDispositivo();
  
   


</script>




