from locust import HttpUser, between, task

class CatalogBrowsingUser(HttpUser):
    wait_time = between(1, 2)
    @task
    def list_products(self):
        self.client.get("/api/products")
