document.addEventListener('alpine:init', () => {
	console.log('Alpine initialized');
	Alpine.data('initData', (pageNo) => ({
		pageNo: pageNo,
		products: {
			data: [],
		},
		init() {
		  this.loadProducts(this.pageNo);
		},
		loadProducts(pageNo) {
			$.getJSON("/api/products?page="+pageNo, (resp) => {
				console.log('Products loaded:', resp);
				this.products = resp;
			});
		},
		addToCart (product){
			addProductToCart(product);
		}	
	}));
		
});