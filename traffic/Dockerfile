FROM python:3.8-slim-buster

WORKDIR /usr/src/app

COPY requirements.txt .
COPY traffic_simulation.py .

RUN pip3 install -r requirements.txt

EXPOSE 5000


CMD [ "python", "traffic_simulation.py"]