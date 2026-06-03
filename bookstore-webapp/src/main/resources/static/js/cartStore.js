const BOOK_STORE_KEY = "BOOK_STORE";


const getCart = () => {
	let cart = localStorage.getItem(BOOK_STORE_KEY);
	if (!cart) {
		cart = JSON.stringify({ items: [], totalAmount: 0 });
		localStorage.setItem(BOOK_STORE_KEY, cart);
	}
	return JSON.parse(cart);
}


const addProductToCart = (product) => {
	let cart = getCart();
	let cartItem = cart.items.find(item => item.code == product.code)
	if(cartItem) {
		cartItem.quantity = cartItem.quantity + 1;
	} else {
		cart.items.push(Object.assign({}, product, { quantity: 1 }));
	}
	localStorage.setItem(BOOK_STORE_KEY, JSON.stringify(cart));
	updateCartItemCount();
}

const updateProductQuantity = function(code, quantity) {
    let cart = getCart();
    if(quantity < 1) {
        cart.items = cart.items.filter(itemModel => itemModel.code !== code);
    } else {
        let cartItem = cart.items.find(itemModel => itemModel.code === code);
        if (cartItem) {
            cartItem.quantity = parseInt(quantity);
        } else {
            console.log("Product code is not already in Cart, ignoring")
        }
    }
    localStorage.setItem(BOOK_STORE_KEY, JSON.stringify(cart));
    updateCartItemCount();
}

const deleteCart = function() {
    localStorage.removeItem(BOOK_STORE_KEY)
    updateCartItemCount();
}


function updateCartItemCount() {
	 let cart = getCart();
	 let count = 0;
	 cart.items.forEach(item => {
		count = count + item.quantity;
	 });
	 $('#cart-item-count').text(count);
}


function getCartTotal() {
    let cart = getCart();
    let totalAmount = 0;
    cart.items.forEach(item => {
        totalAmount = totalAmount + (item.price * item.quantity);
    });
    return totalAmount;
}
	