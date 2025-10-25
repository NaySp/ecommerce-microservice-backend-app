from locust import HttpUser, between, task

class CheckoutUser(HttpUser):
    wait_time = between(1, 2)
    
    @task(1)
    def list_orders(self):
        self.client.get("/order-service/api/orders")
    
    @task(1)
    def get_order_detail(self):
        # Obtener una orden aleatoria (asumiendo IDs 1, 2, 3, etc)
        import random
        order_id = random.randint(1, 10)
        self.client.get(f"/order-service/api/orders/{order_id}")
