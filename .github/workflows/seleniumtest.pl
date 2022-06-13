use Selenium::Remote::Driver;
 
my $driver = Selenium::Remote::Driver->new('browser_name' => 'chrome');
$driver->get('http://www.google.com');
print $driver->get_title();
$driver->quit();

