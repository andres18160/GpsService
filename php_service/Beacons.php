<?php
include "config.php";
include "utils.php";


$dbConn =  connect($db);



// Registra una nueva coordenada GPS
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{

    $input =$_POST;

    $detail=json_decode($input['Details']);

  // print_r($detail);
 //exit();





    $sql = "INSERT INTO beacons
          (IdPhone, DateTime)
          VALUES
          (:IdPhone,:FechaHora)";
    $statement = $dbConn->prepare($sql);
    $statement->bindParam(":IdPhone", $input["IdPhone"], PDO::PARAM_STR);
    $statement->bindParam(":FechaHora", $input["FechaHora"], PDO::PARAM_STR);
    $statement->execute();
    $postId = $dbConn->lastInsertId();
    if($postId)
    {

      for($i=0;$i<3;$i++){
            $sql = "INSERT INTO beacons_detail
            (IdHead, Beacon,distance)
            VALUES
            (:IdHead,:Beacon,:distance)";
            $statement = $dbConn->prepare($sql);
            $statement->bindParam(":IdHead", $postId, PDO::PARAM_STR);
            $statement->bindParam(":Beacon", $detail[$i]->Id, PDO::PARAM_STR);
            $statement->bindParam(":distance",number_format($detail[$i]->distancia,2), PDO::PARAM_STR);
            $statement->execute();
      }
      $input['id'] = $postId;
      header("HTTP/1.1 200 OK");
      echo json_encode($input);
      exit();
	 }
}





//En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");

?>