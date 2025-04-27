// File: src/components/CheckoutPopup.jsx

import React, { useContext } from 'react';
import { Modal, Button } from 'react-bootstrap';
import AppContext from '../Context/Context';
import axios from '../axios';
import { useNavigate } from 'react-router-dom'; // ⬅️ добавили

const CheckoutPopup = ({ show, handleClose, cartItems, totalPrice }) => {
  const { clearCart } = useContext(AppContext);
  const navigate = useNavigate(); // ⬅️ создаем навигатор

  const handleConfirmPurchase = async () => {
    try {
      await axios.post(
        'cart/checkout',
        cartItems.map(({ product, quantity }) => ({
          productId: product.id,
          quantity,
        }))
      );

      clearCart();
      handleClose();
      alert('Purchase completed successfully!');

      navigate('/'); // ⬅️ переход на главную страницу
    } catch (err) {
      console.error('Checkout failed:', err);
      alert('Failed to complete purchase.');
    }
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Checkout</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        {cartItems.map(({ product, quantity }) => (
          <div key={product.id} className="d-flex align-items-center mb-3">
            <img
              src={product.imageUrl}
              alt={product.name}
              style={{
                width: 100,
                height: 100,
                objectFit: 'cover',
                borderRadius: 4,
              }}
            />
            <div className="ms-3">
              <h6 style={{ margin: 0 }}>{product.name}</h6>
              <p style={{ margin: '4px 0' }}>Quantity: {quantity}</p>
              <p style={{ margin: 0 }}>
                Price: ${(product.price * quantity).toFixed(2)}
              </p>
            </div>
          </div>
        ))}

        <hr />

        <h5 style={{ textAlign: 'right' }}>
          Total: ${totalPrice.toFixed(2)}
        </h5>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={handleConfirmPurchase}>
          Confirm Purchase
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default CheckoutPopup;
