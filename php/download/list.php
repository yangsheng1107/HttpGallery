<?php

// 搜尋資料夾中所有檔名字串結尾為 .gif、.jpg、.png 檔案路徑
//(※這邊要注意，若副檔名大小寫不一樣，會搜尋不到，像 .GIF 、 .gIf 或 .giF 都會被忽略掉)
$files = glob("{*.gif,*.jpg,*.png}",GLOB_BRACE);
$count = 0;
//print each file name
foreach($files as $file)
{
	$response[$count]["count"] = $count;
	$response[$count]["filename"] = $file;
	$count++;
}
die(json_encode(array('images' => $response)));
?>