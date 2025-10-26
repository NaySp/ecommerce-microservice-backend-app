from locust import HttpUser, between, task

class ProductUser(HttpUser):
    wait_time = between(1, 2)
    
    @task(3)
    def browse_catalog(self):
        """Listar todos los productos"""
        self.client.get("/product-service/api/products")
    
    @task(1)
    def get_product_detail(self):
        """Obtener producto específico"""
        self.client.get("/product-service/api/products/1")
