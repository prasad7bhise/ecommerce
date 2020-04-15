const express = require('express')
const db = require('../db')
const utils = require('../utils')
const multer = require('multer')
const router = express.Router()
const upload = multer({dest: 'images'})

router.get('/', (request, response) => {
    const statement = `select product.id, product.title, description, price, image, category.title, category_id from product, category where product.category_id = category.id`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

router.post('/', upload.single('image'), (request, response) => {
    console.log('inserting product')
    const file = request.file.filename
    const {title, description, price, category_id} = request.body
    const statement = `insert into product (title, description, price, category_id, image) values ('${title}', '${description}', '${price}', '${category_id}', '${file}')`
    console.log(statement)
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

router.put('/:id', (request, response) => {
    const id = request.params.id
    const {title, description, price, category_id} = request.body
    const statement = `update product set title = '${title}', description = '${description}', price = '${price}', category_id = '${category_id}' where id = ${id}`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

router.delete('/:id', (request, response) => {
    const id = request.params.id
    const statement = `delete from product where id = ${id}`
    const connection = db.createConnection()
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error, data))
    })
})

module.exports = router