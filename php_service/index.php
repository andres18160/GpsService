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
                    <canvas id="myCanvas" width="4008" height="2834" style="border:1px solid #000000;"></canvas>
                </div>
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
            id:"00000000-0000-0000-0000-000000010000",
            num:1,
            x:1800,
            y:1060,
            distance:"",
            color:'#FF0000'
        },
        {            
            id:"72bd45c1-21f8-4814-a6e1-d836d3974880",
            num:2,
            x:1560,
            y:1140,
            distance:"",
            color:'#FF0000'
        },
        {            
            id:"00000000-0000-0000-0000-000000001000",
            num:3,
            x:1800,
            y:1250,
            distance:"",
            color:'#FF0000'
        },
        {            
            id:"0eae3df4-102f-444f-8007-d712ffe83724",//ROSA
            num:4,
            x:1560,
            y:1360,
            distance:"",
            color:'#D98880'
        },
        {            
            id:"acc0eeff-9287-46bd-9d79-8c3f103e6684",//AMARILLO
            num:5,
            x:1800,
            y:1470,
            distance:"",
            color:'#F4D03F'
        },
        {            
            id:"1d13b961-752e-41c3-9f0f-53ec8f623cac",//ROJO
            num:6,
            x:1560,
            y:1580,
            distance:"",
            color:'#A93226'
        }
        
        
    ];

    var ctx = document.getElementById('myCanvas').getContext('2d');
    var imgAnt = new Image();
    imgAnt.src = 'https://www.k-lenda.com/wp-content/themes/klenda/img/klenda/IMG_iBeacon.png';
    var imgDispo = new Image();
    imgDispo.src = 'https://www.shareicon.net/data/256x256/2015/09/25/107071_internet_512x512.png';
    var imgFondo=new Image();
    imgFondo.src='mapa.jpg';





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

            ctx.clearRect(0, 0, 4008, 2834);

            ctx.drawImage(imgFondo, 0,0,4008,2834);
            $.each( beacons, function( key, value ) {            
               ctx.drawImage(imgAnt, value.x, value.y,25,25);
            });      
            var x=0;
            var y=0;
            
            $.each(dispositivos,function(ikey,i){
                $.each( beacons, function( key, value ) {     
                    if(i.Beacon==value.id){
                        x=x+value.x;
                        y=y+value.y;
                        i.x=value.x;
                        i.y=value.y;
                        ctx.beginPath();
                        ctx.arc(i.x,i.y,(4008*i.distance)/100,0,2*Math.PI,false);                       
                        ctx.strokeStyle = value.color;
                        ctx.lineWidth = 2;
                        ctx.stroke();                        
                    }
                }); 
            });  
           
            x=x/3;
            y=y/3;
        
            console.log("Se ejecuto x="+x+" y="+y);
            ctx.drawImage(imgDispo, x, y,50,50);
            setTimeout("location.reload()", 1000);
    }


        function dibujarDispositivoBack()
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

            ctx.drawImage(imgFondo, 0,0,4008,2834);
            $.each( beacons, function( key, value ) {            
               ctx.drawImage(imgAnt, value.x, value.y,25,25);
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
            setTimeout("location.reload()", 1000);
    }

    dibujarDispositivo();
  
   


</script>




