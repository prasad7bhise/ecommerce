const express = require ('express')
const FCM = require ('fcm-node')
const fcm = new FCM('AAAAbOc3yxI:APA91bFsnmkPoT-iwpqIM4kiEqtELqKBpN07S1Dg983F5JVcWvxP75wC48LJkSHfvL2jfieBRQqAemtz8FtZtn6FDGGwdI56WzXN2qEc58F-oSfSGpKi8AfR01YBdnj3gAK5PJip9upJ')
const utils = require('../utils')
const router = express.Router()
const db = require('../db')

router.post('/', (request, response)=> {
    const {title, body} = request.body
    console.log(request.body)
    const statement = `select device_token from user`
    const connection = db.createConnection()
    connection.query(statement, (error, tokens)=>{
        connection.end()
        if (error){
            response.send(utils.createResult(error,tokens))
        } else {
            for (let index = 0; index < tokens.length; index++){
                const token = tokens [index];
               console.log('token:', token)
                 const message = {
                    to: token ['device_token'],
                    notification: {
                        title: title,
                        body: body
                    }  
                 }
             
                 fcm.send(message, (error,info)=>{
                   response.send(utils.createResult(error, info))
                 })
            }

        }
    })

    
})

module.exports = router