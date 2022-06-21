use Selenium::Remote::Driver;
 
my $driver = Selenium::Remote::Driver->new('browser_name' => 'chrome');
$driver->get('http://localhost:8080');
print $driver->get_title();
$driver->quit();

