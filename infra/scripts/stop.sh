docker rm -f $(docker ps -aq)
docker rmi $(docker images -q)
docker system prune

cd /home/ec2-user
rm -rf app
