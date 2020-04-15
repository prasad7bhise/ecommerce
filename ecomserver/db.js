const mysql = require('mysql')

function createConnection(){
     const connection = mysql.createConnection({
         host: 'localhost',
         user: 'dmc',
         password:'dmc',
         database: 'ecomm_final',
         port: 3306
     })
     connection.connect()
     return connection   
}


module.exports ={
    createConnection: createConnection
}
