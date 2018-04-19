//Tony Dinh
//11/13/17
//HashTable

import java.io.*;
import java.util.*;

public class HashTable{
  //implements a hash table using buckets

  private int[] primeSize = {97, 197, 397, 797, 1597, 2999, 6007, 12007, 24007};
  private int primeIndex = 0;
  private int tableSize = primeSize[primeIndex];
  private int wordsInTable = 0;
  private int oldCount = 0; //rehash purposes
  DataList[] table = new DataList[tableSize];

  private class DataList{

    DataItem data;
    DataList next;

    public DataList(DataItem data){
      this.data = data;
      this.next = null;
    }

  }

  public HashTable(){ //constructor

    for(int i = 0; i < tableSize; i++){
      table[i] = null;
    }

  }

  public int hashKeyGenerator(String wordToHash){ //creates hash key, returns int

    int preHashKey = 0;
    int hashKeyHelper = 10;

    for(int i = 0; i < wordToHash.length(); i++){
      preHashKey += wordToHash.charAt(i)*hashKeyHelper;
      hashKeyHelper *= 10; //each letter gets multiplied by 10*n
    }
    if(preHashKey < 0) preHashKey *= -1; //makes sure there's no negative indexes

    return preHashKey%tableSize;

  }

  public void add(String word){ //adds a word to hash table

    String lcword = word.toLowerCase();
    int hashKey = hashKeyGenerator(lcword);

    if(table[hashKey] == null){ //if there's no prior data, creates one at the hash index
      DataItem newData = new DataItem(lcword);
      DataList newList = new DataList(newData);
      table[hashKey] = newList;
      wordsInTable++;

      if(oldCount != 0) { //this runs only after rehash to make sure the count is added
        table[hashKey].data.count = oldCount;
        oldCount = 0; //set count to 0 so that this isn't accessed unless during rehash
      }               //only method that changes oldCount from 0 is rehash

    }

    else{ //if index is already taken or word is already in the table

      DataList curr = table[hashKey];

      while(curr != null){

        if (curr.data.word.equals(lcword)){
          curr.data.count++;
          break;
        }

        //table[hashKey] is a DataList, table[hashKey].data is a
        //DataItem, table[hashKey].data.word/count will give you
        //an int or a word

        if (curr.next == null){
          DataItem newDataOnList = new DataItem(lcword);
          DataList newList = new DataList(newDataOnList);
          curr.next = newList; //if there's no next list, creates a new one containing new word

          if(oldCount != 0) {
            curr.next.data.count = oldCount;
            oldCount = 0;
          }

          wordsInTable++;
          break;
        }

        curr = curr.next; //if it does have a next, set a pointer to it and start over
      }

    }

    if((100*wordsInTable/tableSize) >= 50){ //if words in table fill 50% of the table
      //System.out.println("Rehashing...");
      rehash();
    }

  }

  private void rehash(){ //rehashes data to new table
    int oldSize = tableSize;
    DataList[] oldTable = table;
    tableSize = primeSize[++primeIndex]; //works
    table = new DataList[tableSize];
    wordsInTable = 0; //wordsInTable will repopulate during rehashing

    for(int i = 0; i < oldSize; i++){

      DataList curr = oldTable[i];

      while(curr != null){
        oldCount = curr.data.count; //sets oldCount to current data count
        add(curr.data.word);
        curr = curr.next;
      }

    }
  }

  public void delete(String word){ //removes word from hash table

    String lcword = word.toLowerCase();
    int hashKey = hashKeyGenerator(lcword);
    DataList curr = table[hashKey];

    while(curr != null){

      if(curr.data.word.equals(lcword)){
        table[hashKey] = curr.next; //sets the current DataList to the next (removes it)
        wordsInTable--;
        return;
      }
      curr = curr.next; //check the next list
    }
    return;
  }

  public DataItem highcount(){ //returns the DataItem with the highest count

    int mostOccured = 0;
    int thing = 0; //place holder
    DataItem sendBack = new DataItem(""); //empty DataItem

    for (int i = 0; i < tableSize; i++){
      DataList curr = table[i];

      while(curr != null){
        thing = curr.data.count;

        if(thing > mostOccured){ //replaces highest count and sets to thing
          mostOccured = thing;
          sendBack = curr.data; //only when the thing is greater than does it set sendBack to the
        }                       //current data
        curr = curr.next;
      }
    }
    return sendBack;
  }
}
