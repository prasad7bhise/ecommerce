const express = require('express')
const db = require('../db')
const utils = require('../utils')


const router = express.Router()

router.get('/user/:id', (request,response) => {
    const id = request.params.id

    const connection = db.createConnection()
    const statement = ` select product.id as productId,product.title as title,product.description as description,product.price as price,product.category_id as categoryId,product.image as image, cart.id as cartId, cart.quantity as quantity from cart, product where cart.productid = product.id and userId = ${id}`
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error,data))
    })
})

router.post('/user/:id', (request,response) => {
    const id = request.params.id
    const {productId, quantity} = request.body


    const connection = db.createConnection()

    const statement1 = `select * from cart where userId = ${id} and productId = ${productId}`
    connection.query(statement1, (error,items)=> {
        if (items.length == 0){
            const statement = `insert into cart (userId, productId, quantity) values (${id}, ${productId}, ${quantity})`
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error,data))
    })
        }else {
            connection.end()
            response.send(utils.createResult(error,items))
        }
    })
    
})

router.put('/user/:id', (request,response) => {
    const id = request.params.id
    const { quantity} = request.body


    const connection = db.createConnection()
    const statement = `update cart set quantity = ${quantity} where id = ${id}`
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error,data))
    })
})



router.delete('/:id', (request,response) => {
    const id = request.params.id

    const connection = db.createConnection()
    const statement = `delete from cart where id = ${id}`
    connection.query(statement, (error, data) => {
        connection.end()
        response.send(utils.createResult(error,data))
    })
})


module.exports = router