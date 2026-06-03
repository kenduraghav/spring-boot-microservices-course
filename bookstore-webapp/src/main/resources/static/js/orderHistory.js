document.addEventListener('alpine:init',() => {
	
	Alpine.data('initData',() => ({
		orders: [],
		init() {
			this.getOrderSummary();
		},		
		getOrderSummary(){
			$.getJSON("/api/orders", (resp) => {
				this.orders = resp;
			});
		},
		viewOrderDetails(orderNumber){
			window.location="/orders/"+orderNumber;
		}
	}))
});