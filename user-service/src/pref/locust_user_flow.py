from locust import HttpUser, between, task

class UserFlowUser(HttpUser):
    wait_time = between(1, 2)
    
    @task(1)
    def list_users(self):
        self.client.get("/user-service/api/users")
    
    @task(1)
    def get_user_detail(self):
        import random
        user_id = random.randint(1, 5)
        self.client.get(f"/user-service/api/users/{user_id}")