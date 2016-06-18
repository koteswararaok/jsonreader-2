
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.util.*;


import java.util.*;
import java.io.*;

class jsonRW
{


    public static void main (String[]argv)
    {

        class Listings
        {
            String title;
            String manufacturer;
            String currency;
            String price;
        }

        class Products
        {
            String productName;
            String manufacturer;
            String model;
            String dateOfRelease;
            String family;
        }

        class Result
        {
            String productName;
            Vector < Listings > listings;
        }

        
        JSONParser parser = new JSONParser ();
//        TRIED following but didnt work....
        //parser.configure(parser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        
        

        try
        {

            BufferedReader br =  new BufferedReader (new FileReader ("listings.txt"));
            String line;
            Vector <Listings> vListings = new Vector<Listings>(1000);
            while ((line = br.readLine ()) != null)
            {
                Listings l = new Listings ();

                Object obj = parser.parse (line);

                JSONObject jsonObject = (JSONObject) obj;

                String name = (String) jsonObject.get ("title");

                String man = (String) jsonObject.get ("manufacturer");

                String cur = (String) jsonObject.get ("currency");

                String pri = (String) jsonObject.get ("price");

                l.title = name;
                l.manufacturer = man;
                l.currency = cur;
                l.price = pri;
                vListings.add (l);
            }
            
    /*        System.out.println("############################################### LISTINGS ##################################################\n");

            for(int i=0;i<vListings.size();i++)
            {
                System.out.println(vListings.elementAt(i).title);
                System.out.println(vListings.elementAt(i).manufacturer);
                System.out.println(vListings.elementAt(i).currency);
                System.out.println(vListings.elementAt(i).price);
            }*/
  



            BufferedReader br1 = new BufferedReader (new FileReader("products.txt"));
            Vector <Products> vProducts = new Vector<Products>(1000);
            while ((line = br1.readLine ()) != null)
            {
                Products pr = new Products();

                Object obj = parser.parse (line);

                JSONObject jsonObject = (JSONObject) obj;

                String name = (String) jsonObject.get ("product_name");

                String man = (String) jsonObject.get ("manufacturer");

                String model = (String) jsonObject.get ("model");

                String family = (String) jsonObject.get("family");

                String dateR = (String) jsonObject.get ("announced-date"); //announced-date

                pr.productName = name;
                pr.manufacturer = man;
                pr.model= model;
                pr.dateOfRelease=dateR;
                pr.family = family;
    
                vProducts.add (pr);
            }          
        
            /*System.out.println("############################################### PRODUCTS  ##################################################\n");

            for(int i=0;i<vProducts.size();i++)
            {                
                System.out.println(vProducts.elementAt(i).productName);
                System.out.println(vProducts.elementAt(i).manufacturer);
                System.out.println(vProducts.elementAt(i).model);
                System.out.println(vProducts.elementAt(i).dateOfRelease);
                System.out.println(vProducts.elementAt(i).family);
            }
  */



            Vector<Result> results = new Vector<Result>(1000);
            
            for(int i=0;i<vProducts.size();i++)
            {
                Result r = new Result();
                r.productName= vProducts.elementAt(i).productName;
                r.listings = new Vector<Listings>(vListings.size());
                for(int j=0;j<vListings.size();j++)
                {
                    if( vProducts.elementAt(i).manufacturer.equalsIgnoreCase(vListings.elementAt(j).manufacturer)== true
                            && vListings.elementAt(j).title.contains(vProducts.elementAt(i).model))
                    {
                        if(vProducts.elementAt(i).family!=null && vListings.elementAt(j).title.contains(vProducts.elementAt(i).family))
                        {
                            r.listings.add(vListings.elementAt(j));
                        }
                    }
                }
                results.add(r);
            }
            
            File file = new File("output.txt");
            // creates the file
            file.createNewFile();
            // creates a FileWriter Object
            FileWriter writer = new FileWriter(file); 

            StringBuilder data = new StringBuilder();
            //data.append("{");
            for(int i=0;i<results.size();i++)
            {
                if(results.elementAt(i)!=null  && results.elementAt(i).listings.size()>0)
                {  
                    data.append("{\"product_name\":\"");
                    data.append(results.elementAt(i).productName);

                    data.append("\",\"listings\":[");

                    for(int j=0;j<results.elementAt(i).listings.size();j++)
                    {
                        data.append("{\"title\":\"");
                        String title = results.elementAt(i).listings.elementAt(j).title;
                        data.append(title);
                        data.append("\",\"manufacturer\":\"");
                        data.append(results.elementAt(i).listings.elementAt(j).manufacturer);
                        data.append("\",\"currency\":\"");
                        data.append(results.elementAt(i).listings.elementAt(j).currency);
                        data.append("\",\"price\":\"");
                        data.append(results.elementAt(i).listings.elementAt(j).price);
                        data.append("\"},");
                    }

                    data.setLength(data.length() - 1);
                    data.append("]}");
                    data.append("\n");
                    //data.append(System.getProperty("line.separator"));
                }
                else
                {
//                    System.out.println(" SCHADE::  Warum bin ich hier?");
                }

                writer.write(data.toString()); 
                data.setLength(0);

            }

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace ();
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }
        catch (ParseException e)
        {
            e.printStackTrace ();
        }
    }

}
