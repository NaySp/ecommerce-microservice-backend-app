from locust import HttpUser, between, task

class CheckoutUser(HttpUser):
    wait_time = between(1, 2)
    @task
    def place_order(self):
        self.client.post("/api/orders", json={"userId": 1, "productId": 101, "quantity": 2})
