<?php
require_once "db.inc.php";
/*
 * poll
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

    echo '<toucan username="sarteleb" gameover="false">';


}



