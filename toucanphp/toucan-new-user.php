<?php
/*
 * Toucan application new user
 */
require_once "db.inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
    echo '<toucan status="no" msg="magic" />';
    exit;
}

// Process in a function
process($_GET['user'], $_GET['password']);

/**
 * Process the query
 * @param $user to insert
 * @param $password the users password
 */
function process($user, $password) {
    // Connect to the database
    $pdo = pdo_connect();

    $query = "insert into toucanuser(user, password) values('$user', '$password')";



    if(!$pdo->query($query)) {
        echo '<toucan status="no" msg="insertfail">' . $query . '</toucan>'; // Username is already in use
        exit;
    }

    echo '<toucan status="yes"/>';

    exit;

}
