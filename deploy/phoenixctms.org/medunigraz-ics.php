<?php
/**
 * Forwards a Google Calendar duty-roster subscribe request to Phoenix.
 * Place this file in the phoenixctms.org document root.
 * Public URL: https://www.phoenixctms.org/medunigraz-ics.php?jwt=...
 *
 * Only the jwt query parameter is forwarded. No caller-supplied host is used.
 */
$upstream = 'https://phoenix.medunigraz.at/rest/dutyrosterturn/ics';

$jwt = isset($_GET['jwt']) ? $_GET['jwt'] : '';
if (!is_string($jwt) || $jwt === '' || strlen($jwt) > 8192) {
	http_response_code(400);
	header('Content-Type: text/plain; charset=UTF-8');
	echo 'Missing jwt';
	exit;
}

$url = $upstream . '?jwt=' . rawurlencode($jwt);

if (!function_exists('curl_init')) {
	http_response_code(502);
	header('Content-Type: text/plain; charset=UTF-8');
	echo 'Upstream request failed';
	exit;
}

$ch = curl_init($url);
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, false);
curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 15);
curl_setopt($ch, CURLOPT_TIMEOUT, 60);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HEADER, true);
$body = curl_exec($ch);
if ($body === false) {
	http_response_code(502);
	header('Content-Type: text/plain; charset=UTF-8');
	echo 'Upstream request failed';
	curl_close($ch);
	exit;
}
$status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
$headerSize = curl_getinfo($ch, CURLINFO_HEADER_SIZE);
curl_close($ch);

$rawHeaders = substr($body, 0, $headerSize);
$payload = substr($body, $headerSize);
$contentType = 'text/calendar; charset=UTF-8';
foreach (preg_split("/\r\n|\n|\r/", $rawHeaders) as $headerLine) {
	if (stripos($headerLine, 'Content-Type:') === 0) {
		$contentType = trim(substr($headerLine, strlen('Content-Type:')));
		break;
	}
}

http_response_code($status > 0 ? $status : 502);
header('Content-Type: ' . $contentType);
echo $payload;
