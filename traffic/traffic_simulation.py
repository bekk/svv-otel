from flask import Flask, jsonify
import time
import requests
import logging
import random

app = Flask(__name__)

logging.basicConfig(level=logging.DEBUG)


endpoints = [
    {"url": "http://kundereg:8080/api/hentKunder", "method": "GET"},
    {"url": "http://kundereg:8080/api/switchOnOffServer", "method": "POST", "params": {"withFact": "false"}},
    {"url": "http://foererkort:8080/api/harFoererkort", "method": "GET"},
    {"url": "http://foererkort:8080/api/switchOnOffServer", "method": "POST", "params": {"name": "Gunnar",
                                                                                         "withFact": "true"}},
]


def send_request(endpoint):
    try:
        if endpoint['method'] == 'POST':
            response = requests.post(endpoint['url'])
        elif endpoint['method'] == 'GET':
            params = endpoint.get('params', {})
            response = requests.get(endpoint['url'], params=params)
        logging.info(f'Success: {response.url}, Status Code: {response.status_code}')
    except requests.RequestException as e:
        logging.error(f'Failed: {e}')


@app.route("/start")
def start_simulation():
    end_time = time.time() + 1 * 60

    while time.time() < end_time:
        endpoint = random.choice(endpoints)
        send_request(endpoint)
        time.sleep(3)

    return jsonify(status='Traffic simulation completed'), 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)