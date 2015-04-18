<?php
require_once "db.inc.php";
/*
 * Hatter application catalog
 */
require_once "db.inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
    echo '<toucan status="no" msg="magic" />';
    exit;
}

// Process in a function
process();

/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function process() {
    // Connect to the database
    $pdo = pdo_connect();


    $query = "select * from toucanactiveuser";

    $rows = $pdo->query($query);

    if($rows->rowCount() == 2)
    {
        echo '<toucan status="yes" msg="both players logged in"/>';
    }
    else{
        echo '<toucan status="no" msg="both players not logged in"/>';
    }



}



