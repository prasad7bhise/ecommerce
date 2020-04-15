const express = require('express')
const db = require('../db')
const utils = require('../utils')
const cryptoJS = require('crypto-js')

const router = express.Router()

router.get('/', (request, response) => {
    const statement = `select id, name, email, status from user`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

router.post('/register', (request, response) => {
    const {name, email, password} = request.body
    const encryptedPassword = cryptoJS.MD5(password)
    const statement = `insert into user (name, email, password) values ('${name}', '${email}', '${encryptedPassword}')`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

router.post('/login', (request, response) => {
    const {email, password, device_token} = request.body
    console.log(request.body)

    const encryptedPassword = cryptoJS.MD5(password)
    let statement = `select id, name, email, status from user where email = '${email}' and password = '${encryptedPassword}'`
    const connection = db.createConnection()
    connection.query(statement, (error, users) => {
        
        if (users.length == 0) {
            connection.end()
            response.send(utils.createResult('invalid email or password'))
        } else {
            const user = users[0]
            if (user['status'] == 0) {
                connection.end()
                // user is disabled
                response.send(utils.createResult('Your account is disabled. Please contact Administrator.'))
            } else {
                // update the device token (it will be used later to send the notifications)
                
                statement = `update user set device_token = '${device_token}' where id = ${user.id}`
                connection.query(statement, (error, data) => {
                    connection.end()
                    response.send(utils.createResult(error, user))
                    
                })
            }
        }
    })
})

router.put('/toggle-status/:id', (request, response) => {
    const id = request.params.id
    const {status} = request.body
    const statement = `update user set status = ${status} where id = ${id}`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

router.delete('/:id', (request, response) => {
    const id = request.params.id
    const statement = `delete from user where id = ${id}`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

module.exports = router