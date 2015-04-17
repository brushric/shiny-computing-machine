<?php
require_once "db.inc.php";
/*
 * Game state
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
 */
function process($user) {
    // Connect to the database
    $pdo = pdo_connect();



    $selectQuery = "select * from toucangamestate";
    $rows = $pdo->query($selectQuery);

    $query = "select * from toucanactiveuser";
    $userRows = $pdo->query($query);

    if($userRows->rowCount() == 1)
    {
        echo '<toucan status="no">Only 1 user is active<toucan />';
        exit;
    }

    else if($userRows->rowCount() == 2 and $rows->rowCount() == 0)
    {
        echo '<toucan status="yes">2 active users and no current game<toucan />';
        exit;
    }

    else
    {
        $row = $rows->fetch();
        if($row['user'] != $user) {
            $win = $row['win'];
            if($win == 1)
                echo '<toucan status="win" msg="Game state has changed" />';
            else
                echo '<toucan status="update" msg="Game state has changed" />';
        }
        else
            echo '<toucan status="no" msg="Game state has not changed"/>';
    }





}



