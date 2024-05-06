to build docker image: docker build -t todo-list-image . OR 
docker build --platform linux/arm64 -t todo-list-image .

to save image as archive docker save -o todo-list-telegram-bot.tar todo-list-image:latest

to copy archive to server scp -i ssh-key.key todo-list-telegram-bot.tar user@host:/home/ubuntu/todo-list-telegram-bot

connect to server ssh -i /ssh-key.key user@host

to load archive docker load -i todo-list-telegram-bot.tar

to copy file to server scp -i ssh-key.key docker-compose.yml user@host:/home/ubuntu/todo-list-telegram-bot

to run docker-compose up -d