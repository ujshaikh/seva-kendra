To access a MySQL shell inside a Docker container using the command line (CMD), you need to follow a series of steps. Below is a step-by-step guide on how to do this:

### Step 1: Pull the MySQL Docker Image (if not already done)

If you haven't already pulled the MySQL image from Docker Hub, you can do so using the following command:

```bash
docker pull mysql:latest
```

### Step 2: Run the MySQL Container

You can run a MySQL container using the following command. Replace `your_password` with a secure password of your choice:

```bash
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=your_password -d mysql:latest
```

- `--name mysql-container`: Names the container "mysql-container."
- `-e MYSQL_ROOT_PASSWORD=your_password`: Sets the root password for MySQL.
- `-d`: Runs the container in detached mode (in the background).

### Step 3: Access the MySQL Shell

Once your MySQL container is running, you can access the MySQL shell by executing the following command:

```bash
docker exec -it mysql-container mysql -u root -p
```

- `docker exec`: Executes a command in a running container.
- `-it`: Runs the container in interactive mode with a terminal.
- `mysql-container`: The name of your MySQL container.
- `mysql -u root -p`: This command runs the MySQL client as the root user and prompts for the password.

### Step 4: Enter Your Password

After executing the command, you will be prompted to enter the password for the root user. Enter the password you specified when you created the container.

### Step 5: Use the MySQL Shell

Once you have successfully entered your password, you will be in the MySQL shell, and you can start executing MySQL commands. For example:

```sql
SHOW DATABASES;
```

### Example Commands

Here are some basic commands you might use in the MySQL shell:

- **Create a Database:**
  ```sql
  CREATE DATABASE example_db;
  ```

- **Use a Database:**
  ```sql
  USE example_db;
  ```

- **Create a Table:**
  ```sql
  CREATE TABLE users (
      id INT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(100) NOT NULL
  );
  ```

- **Insert Data:**
  ```sql
  INSERT INTO users (name) VALUES ('John Doe');
  ```

- **Query Data:**
  ```sql
  SELECT * FROM users;
  ```

### Step 6: Exit the MySQL Shell

When you are finished using the MySQL shell, you can exit by typing:

```sql
EXIT;
```

### Step 7: Stopping and Removing the Container

To stop the running MySQL container, use:

```bash
docker stop mysql-container
```

To remove the container after stopping it, use:

```bash
docker rm mysql-container
```

### Conclusion

You can now access the MySQL shell running inside a Docker container using CMD. If you encounter any issues or have specific requirements, feel free to ask!