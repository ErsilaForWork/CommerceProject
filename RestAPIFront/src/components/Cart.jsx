import React, { useContext } from "react";
 import AppContext from "../Context/Context";
 import CheckoutPopup from "./CheckoutPopup";
import { Button } from 'react-bootstrap';

 const Cart = () => {
  const { cart, removeFromCart, updateQuantity, clearCart } = useContext(AppContext);
  const [showModal, setShowModal] = React.useState(false);
  const safeCart = Array.isArray(cart) ? cart : [];
  const totalPrice = safeCart.reduce((sum, item) => sum + item.product.price * item.quantity, 0);


   return (
     <div className="cart-container">
       <div className="shopping-cart">
        <div className="title">Shopping Bag</div>
         {cart.length === 0 ? (
           <div className="empty" style={{ textAlign: "left", padding: "2rem" }}>
             <h4>Your cart is empty</h4>
           </div>
         ) : (
           <>
            {cart.map(({ product, quantity }) => (
              <li key={product.id} className="cart-item">
                <div className="item" style={{ display: 'flex', alignItems: 'center' }}>
                 <img src={product.imageUrl} alt={product.name} className="cart-item-image" />
                  <div className="description">
                    <span>{product.brand}</span>
                    <span>{product.name}</span>
                  </div>
                 <div className="quantity">
                    <button onClick={() => updateQuantity(product.id, quantity + 1)}>+</button>
                    <input readOnly value={quantity} />
                    <button onClick={() => updateQuantity(product.id, Math.max(quantity - 1, 1))}>-</button>
                  </div>
                  <div className="total-price">${product.price * quantity}</div>
                  <button onClick={() => removeFromCart(product.id)}>🗑️</button>
                </div>
              </li>
            ))}
             <div className="total">Total: ${totalPrice}</div>
             <Button
               className="btn btn-primary"
               style={{ width: "100%" }}
              onClick={() => setShowModal(true)}
             >
               Checkout
             </Button>
           </>
         )}
       </div>
       <CheckoutPopup
         show={showModal}
         handleClose={() => setShowModal(false)}
         cartItems={cart}
        totalPrice={totalPrice}
        handleCheckout={async () => {
          // your existing checkout logic, then clearCart()
          await clearCart();
          setShowModal(false);
        }}
       />
     </div>
   );
};

export default Cart;