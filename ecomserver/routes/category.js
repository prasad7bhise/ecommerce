const express = require('express')
const db = require('../db')
const utils = require('../utils')

const router = express.Router()

router.get('/', (request, response) => {
    const statement = `select * from category`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

router.post('/', (request, response) => {
    const {title} = request.body
    const statement = `insert into category (title) values ('${title}')`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

router.put('/:id', (request, response) => {
    const id = request.params.id
    const {title} = request.body
    const statement = `update category set title = '${title}' where id = ${id}`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

router.delete('/:id', (request, response) => {
    const id = request.params.id
    const statement = `delete from category where id = ${id}`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

module.exports = router