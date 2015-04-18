<?php
/*
 * Toucan game over
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
 */
function process() {
    // Connect to the database
    $pdo = pdo_connect();

    $query = "DELETE FROM toucanactiveuser";


    $pdo->query($query);

    $query = "DELETE FROM toucangamestate";

    $pdo->query($query);


    exit;

}
