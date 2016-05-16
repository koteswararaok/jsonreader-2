sudo apt-get install qt
cd ~
mkdir rsadhu
git clone https://github.com/rsadhu/jsonreader.git
cd jsonreader
qmake
make 
./jsonreader 
cat output.txt


