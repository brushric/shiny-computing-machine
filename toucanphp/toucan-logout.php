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
process($_GET['user']);

/**
 * Process the query
 * @param $user the user to look for
 */
function process($user) {
    // Connect to the database
    $pdo = pdo_connect();


    $query = "delete from toucanactiveuser where user='$user'";
    if(!$pdo->query($query))
        echo '<toucan status="no" msg="logout fail">';
    else
        echo '<toucan status = "yes" msg="logged out">';




}



