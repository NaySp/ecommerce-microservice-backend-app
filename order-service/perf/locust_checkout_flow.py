from locust import HttpUser, between, task

class CheckoutUser(HttpUser):
    wait_time = between(1, 2)
    
    @task(3)
    def list_orders(self):
        """Listar todas las órdenes - siempre exitoso"""
        self.client.get("/order-service/api/orders")
    
    @task(1)
    def get_order_detail(self):
        """Obtener orden específica - usar solo IDs existentes"""
        # BD tiene orden ID 1 confirmada
        self.client.get("/order-service/api/orders/1")
