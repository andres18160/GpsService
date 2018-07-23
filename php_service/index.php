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
                    <canvas id="myCanvas" width="600" height="200" style="border:1px solid #000000;"></canvas>
                </div>
            </section>
            <?php
           /*  $sql = "SELECT * FROM `beacons_detail` WHERE IdHead=(SELECT MAX(Id) FROM beacons WHERE IdPhone='3079c6c4bd209faf')";
             $statement = $dbConn->prepare($sql);
             $statement->execute();
           $response=$statement -> fetchAll();             
           echo "<pre>";
           print_r($response);*/

            ?>
        </body>
</html>
<script>

    var beacons=[
        {            
            id:"00000000-0000-0000-0000-000000010000",
            num:1,
            x:0,
            y:0,
            distance:""
        },
        {            
            id:"72bd45c1-21f8-4814-a6e1-d836d3974880",
            num:2,
            x:100,
            y:180,
            distance:""
        },
        {            
            id:"00000000-0000-0000-0000-000000001000",
            num:3,
            x:200,
            y:0,
            distance:""
        },
        {            
            id:"0eae3df4-102f-444f-8007-d712ffe83724",
            num:4,
            x:300,
            y:180,
            distance:""
        },
        {            
            id:"acc0eeff-9287-46bd-9d79-8c3f103e6684",
            num:5,
            x:400,
            y:0,
            distance:""
        },
        {            
            id:"1d13b961-752e-41c3-9f0f-53ec8f623cac",
            num:6,
            x:500,
            y:180,
            distance:""
        }
        
        
    ]
     var ctx = document.getElementById('myCanvas').getContext('2d');
    var imgAnt = new Image();
        imgAnt.src = 'https://cdn-img.easyicon.net/png/11836/1183688.gif';
        var imgDispo = new Image();
        imgDispo.src = 'https://www.shareicon.net/data/256x256/2015/09/25/107071_internet_512x512.png';




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

            ctx.clearRect(0, 0, 600, 200);
            $.each( beacons, function( key, value ) {            
            ctx.drawImage(imgAnt, value.x, value.y,20,20);
            });      
            var x=0;
            var y=0;

            $.each(dispositivos,function(ikey,i){
                $.each( beacons, function( key, value ) {     
                    if(i.Beacon==value.id){
                        x=x+value.x;
                        y=y+value.y;
                    }
                }); 
            });  

            x=x/3;
            y=y/3;
        
            console.log("Se ejecuto x="+x+" y="+y);
            ctx.drawImage(imgDispo, x, y,20,20);
            setTimeout("dibujarDispositivo()", 1000);
    }

    dibujarDispositivo();
  
   


</script>




