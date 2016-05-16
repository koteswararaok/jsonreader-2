sudo apt-get install qt
sudo mkdir $(HOME)/rsadhu/
git clone https://github.com/rsadhu/jsonreader.git
cd jsonreader
qmake
make 
./jsonreader 
cat output.txt


