<?php

// �j�M��Ƨ����Ҧ��ɦW�r�굲���� .gif�B.jpg�B.png �ɮ׸��|
//(���o��n�`�N�A�Y���ɦW�j�p�g���@�ˡA�|�j�M����A�� .GIF �B .gIf �� .giF ���|�Q������)
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