<?php
require_once "db.inc.php";
/*
 * Hatter application catalog
 */
require_once "db.inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
    echo '<hatter status="no" msg="magic" />';
    exit;
}

// Process in a function
process($_GET['user'], $_GET['password']);

/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function process($user, $password) {
    // Connect to the database
    $pdo = pdo_connect();


    $query = "select * from toucanuser where user='$user' and password='$password'";
    $rows = $pdo->query($query);
    $userVerify = null;
    $passwordVerify = null;
    foreach($rows as $row ) {
        $userVerify = $row['user'];
        $passwordVerify = $row['password'];


    }

    if($userVerify != null and $passwordVerify != null)
        echo '<toucan status = "yes" msg="logged in">';
    else
        echo '<toucan status="no" msg="login fail">';


}



