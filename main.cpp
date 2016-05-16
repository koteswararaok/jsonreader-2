#include <QApplication>
#include<QDebug>
#include<QFile>
#include<QJsonDocument>
#include<QJsonArray>
#include<QJsonObject>
#include<QVector>

class Listings
{
public:
    QString title;
    QString manufacturer;
    QString currency;
    QString price;
};

class Products
{
public:
    QString productName;
    QString manufacturer;
    QString model;
    QString dateOfRelease;
    QString family;
};

class Result
{
public:
 QString  productName;
 QList<Listings> listing;
};

void getMeListings(char *fileName,QVector<Listings> &listingsArray)
{
    QFile file(fileName);
    file.open(QIODevice::ReadOnly|QIODevice::Text);

    QTextStream in(&file);

    while(!in.atEnd())
    {
        QString val;
        val = in.readLine();
        QJsonDocument jsonResponse = QJsonDocument::fromJson(val.toUtf8());

        QJsonObject jsObj = jsonResponse.object();

        Listings l;
        l.title =  jsObj.value(QString("title")).toString();
        l.manufacturer =  jsObj.value(QString("manufacturer")).toString();
        l.currency =  jsObj.value(QString("currency")).toString();
        l.price =  jsObj.value(QString("price")).toString();
        listingsArray.push_back(l);
    }

    file.close();
}
void getMeProducts(char *fileName, QVector<Products>& productsArray)
{
    QFile file(fileName);
    file.open(QIODevice::ReadOnly|QIODevice::Text);

    QTextStream in(&file);

    while(!in.atEnd())
    {
        QString val;
        val = in.readLine();
        QJsonDocument jsonResponse = QJsonDocument::fromJson(val.toUtf8());
        QJsonObject jsObj = jsonResponse.object();

        Products p;
        p.productName =  jsObj.value(QString("product_name")).toString();
        p.manufacturer =  jsObj.value(QString("manufacturer")).toString();
        p.model =  jsObj.value(QString("model")).toString();
        p.dateOfRelease =  jsObj.value(QString("announced-date")).toString();
        p.family =  jsObj.value(QString("family")).toString();
        productsArray.push_back(p);
    }

    file.close();

}

int main(int argc, char *argv[])
{
    if(argc!=3)
    {
        qDebug()<<" Provide First  Arg as listings.txt :  \n";
        qDebug()<<" Provide Second Arg as product.txt :   \n";
        qDebug()<<"Insufficient args\n";
        return -1;
    }

    QVector<Listings>  listingsArray;
    QVector<Products>  productsArray;
/*#ifndef WIN32
    getMeListings("/home/syilmaz/rsadhu/coding/work/qtCoding/jsonReader/listings.txt",listingsArray);
    getMeProducts("/home/syilmaz/rsadhu/coding/work/qtCoding/jsonReader/products.txt",productsArray);
#elif WIN32
    getMeListings("C:\\Users\\KEREM\\Desktop\\work\\qtCoding\\jsonReader\\listings.txt",listingsArray);
    getMeProducts("C:\\Users\\KEREM\\Desktop\\work\\qtCoding\\jsonReader\\products.txt",productsArray);
#endif*/

    getMeListings(argv[1],listingsArray);
    getMeProducts(argv[2],productsArray);

    QVector<Result> results;
    for(int i=0;i<productsArray.size();i++)
    {
        Result r;
        r.productName= productsArray.at(i).productName;
        for(int j=0;j<listingsArray.size();j++)
        {
            if(
              productsArray.at(i).manufacturer.compare(listingsArray.at(j).manufacturer,Qt::CaseInsensitive)
           && listingsArray.at(j).title.contains(productsArray.at(i).family,Qt::CaseInsensitive)
           && listingsArray.at(j).title.contains(productsArray.at(i).model,Qt::CaseInsensitive))
            {
                r.listing.append(listingsArray.at(j));
            }
        }
        results.push_back(r);
    }

    QFile file("/home/syilmaz/rsadhu/jsonreader/output.txt");
    if(file.open(QIODevice::WriteOnly | QIODevice::Text))
    {

    QByteArray data;
    for(int i=0;i<results.size();i++)
    {
        if(results.at(i).listing.size()>0){
            data.append(results.at(i).productName);

            for(int j=0;j<results.at(i).listing.size();j++)
            {
                data.append(" ");
                data.append(results.at(i).listing.at(j).title);
                data.append(" ");
                data.append(results.at(i).listing.at(j).manufacturer);
                data.append(" ");
                data.append(results.at(i).listing.at(j).currency);
                data.append(" ");
                data.append(results.at(i).listing.at(j).price);
            }
            data.append("\n");
        }
    }
    file.write(data);
    }
    else
        qDebug()<<" doesnt exist ";
    file.close();
    return 0;
}

