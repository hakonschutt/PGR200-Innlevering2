# Client - Server Socket program

Youtube video: https://youtu.be/5yuV5h0iSCQ

To ensure that this program runs as planned, you need to make sure you have initilized the initDatabase class. This class creates all the necassary tables for this server to run smoothly. Start of by running this and then run the server. After the server is up and running you can add as many clients as you want. Each client runs on its own thread.

##### Correct syntax for txt file:
If you want to add element to the txt files. Make sure you dont mess with the file syntax. Given that a lot of parts of this program has dependacies to this syntax, altering it will crash the program. 

    table_name
    column_name/column_name/column_name
    column_dataType/column_dataType/column_dataType
    dataType_length/dataType_length/dataType_length
    PRIMARY_KEY
    FOREIGN_KEYS (column/table/columnInTable)
    data/data/data
    data/data/data
    
### This program has 3 main functions 
1. Search in database
2. Print tables in database
3. Print table content 
 
These are the instructions allowed in the program.  
    
    ----------------------------------------
    OPTION COMMANDS
    ----------------------------------------
    info    Prints the command page
    search  Search in a chosen table
    print   Prints content from table
    table   Prints tables from the database
    quit    Quits the program
    ----------------------------------------
