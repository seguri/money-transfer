import json
import requests
import unittest
from uuid import UUID

URL = 'http://localhost:4567/api/v1'
HEADERS = {'Content-Type': 'application/json'}

ACCOUNTS = [
    dict(email='alice@example.com', balance=500),
    dict(email='brian@example.com', balance=500),
    ]

def is_uuid(s):
    """Check if a string is a valid UUID"""
    try:
        UUID(s)
        return True
    except ValueError:
        return False

class TestEndpoint(unittest.TestCase):

    def test_api_endpoint(self):
        """Get API base url"""
        res = requests.get(URL, headers=HEADERS)
        self.assertEqual(404, res.status_code)

    def test_invalid_enpoint(self):
        """Get invalid endpoint"""
        res = requests.get(URL + '/foo', headers=HEADERS)
        self.assertEqual(404, res.status_code)

class TestAccount(unittest.TestCase):

    def test_empty_account(self):
        """Create an empty Account"""
        res = requests.post(URL + '/accounts', headers=HEADERS).json()
        self.assertEqual(res.get('status'), 400)

    def test_valid_account(self):
        """Create a valid Account"""
        account = requests.post(URL + '/accounts', data=json.dumps(ACCOUNTS[0]), headers=HEADERS).json()
        self.assertTrue(is_uuid(account.get('uuid')))

class TestTransfer(unittest.TestCase):

    def test_empty_transfer(self):
        """Create an empty Account"""
        res = requests.post(URL + '/transfers', headers=HEADERS).json()
        self.assertEqual(res.get('status'), 400)

    def test_invalid_transfer(self):
        """Create an invalid Transfer"""
        data = {"from": "foo", "to": "bar", "amount": 100}
        res = requests.post(URL + '/transfers', data=json.dumps(data), headers=HEADERS).json()
        self.assertEqual(res.get('status'), 404)

    def test_valid_transfer(self):
        """Create a valid Transfer"""
        # Create account 1
        acc1 = requests.post(URL + '/accounts', data=json.dumps(ACCOUNTS[0]), headers=HEADERS).json()
        self.assertTrue(is_uuid(acc1.get('uuid')))
        # Create account 2
        acc2 = requests.post(URL + '/accounts', data=json.dumps(ACCOUNTS[0]), headers=HEADERS).json()
        self.assertTrue(is_uuid(acc2.get('uuid')))
        # Create transfer
        transfer_data = {"from": acc1.get('uuid'), "to": acc2.get('uuid'), "amount": 100}
        transfer = requests.post(URL + '/transfers', data=json.dumps(transfer_data), headers=HEADERS).json()
        self.assertTrue(is_uuid(transfer.get('uuid')))
        # Retrieve account 1 after transfer
        acc1_after = requests.get(URL + '/accounts/' + acc1.get('uuid'), headers=HEADERS).json()
        self.assertEqual(acc1.get('uuid'), acc1_after.get('uuid'))
        # Verify transfer
        self.assertEqual(acc1.get('balance'), acc1_after.get('balance') + 100)

if __name__ == '__main__':
    unittest.main()
