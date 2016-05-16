sudo apt-get --assume-yes  install qt
cd ~
mkdir rsadhu
cd rsadhu
git clone https://github.com/rsadhu/jsonreader.git
cd jsonreader

qmake
make 

./jsonreader  ~/rsadhu/jsonreader/listings.txt ~/rsadhu/jsonreader/products.txt ~/rsadhu/jsonreader/output.txt
cat output.txt
