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
process($_GET['game'], $_GET['win'], $_GET['user']);

/**
 * Process the query
 * @param $game new game state
 */
function process($game, $win, $user) {
    // Connect to the database
    $pdo = pdo_connect();

    $selectQuery = "select * from toucangamestate";
    $rows = $pdo->query($selectQuery);
    if($rows->rowCount() == 0)
    {
        $query = "insert into toucangamestate(game) values('$game', '$win', '$user')";
        if(!$pdo->query($query))
            echo '<toucan status="no" msg="insert fail">' . $query . '</toucan>';
        else
            echo '<toucan status="yes"/>';
    }
    else
    {
        $query = "update toucangamestate set game='$game', win='$win', user='$user'";
        if(!$pdo->query($query))
            echo '<toucan status="no" msg="update fail">' . $query . '</toucan>';
        else
            echo '<toucan status="yes"/>';
    }






}



