<?php
include "config.php";
include "utils.php";


$dbConn =  connect($db);



// Registra una nueva coordenada
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{

    $input =$_POST;


    $sql = "INSERT INTO coordenadas
          (Longitud, Latitud, Mac, FechaHora)
          VALUES
          (:Longitud, :Latitud, :Mac, :FechaHora)";
    $statement = $dbConn->prepare($sql);
    bindAllValues($statement, $input);
    $statement->execute();
    $postId = $dbConn->lastInsertId();
    if($postId)
    {
      $input['id'] = $postId;
      header("HTTP/1.1 200 OK");
      echo json_encode($input);
      exit();
	 }
}



//En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");

?>