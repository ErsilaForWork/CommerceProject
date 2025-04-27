// File: src/Context/Context.jsx

import React, { createContext, useState, useEffect } from 'react';
import axios from '../axios';

const AppContext = createContext({
  data: [],
  isError: '',
  cart: [],
  refreshData: async () => {},
  addToCart: async (productId, quantity) => {},
  updateQuantity: async (productId, quantity) => {},
  removeFromCart: async (productId) => {},
  clearCart: async () => {},
});

export const AppProvider = ({ children }) => {
  const [data, setData] = useState([]);
  const [isError, setIsError] = useState('');
  const [cart, setCart] = useState([]);

  /** 1) Load products list for Home.jsx **/
  const refreshData = async () => {
    try {
      const res = await axios.get('/products');
      setData(res.data);
    } catch (err) {
      console.error('Failed to load products:', err);
      setIsError(err.message);
    }
  };

  /** 2) Fetch cart items + their images **/
  const fetchCart = async () => {
    try {
      const res = await axios.get('/cart/products'); // обращаемся к правильному эндпоинту
      const rawItems = Array.isArray(res.data) ? res.data : [];
  
      // Переводим формат {a: product, b: quantity} → в нормальный вид
      const itemsWithImages = await Promise.all(
        rawItems.map(async (item) => {
          const product = item.a;
          const quantity = item.b;
  
          let imageUrl = 'placeholder-image-url';
          try {
            const imgRes = await axios.get(
              `/product/${product.id}/image`,
              { responseType: 'blob' }
            );
            imageUrl = URL.createObjectURL(imgRes.data);
          } catch (e) {
            console.error(`Error fetching image for product ${product.id}:`, e);
          }
  
          return {
            product: { ...product, imageUrl },
            quantity,
          };
        })
      );
  
      setCart(itemsWithImages);
    } catch (err) {
      console.error('Fetch cart error:', err);
      setCart([]);
    }
  };

  /** 3) Add (or increment) an item in cart **/
  const addToCart = async (productId, quantity = 1) => {
    await axios.post('/cart/product', { productId, quantity });
    await fetchCart();
  };

  /** 4) Update quantity of an existing item **/
  const updateQuantity = async (productId, quantity) => {
    await axios.put(`/cart/product/${productId}`, { quantity });
    await fetchCart();
  };

  /** 5) Remove a single item **/
  const removeFromCart = async (productId) => {
    await axios.delete(`/cart/product/${productId}`);
    await fetchCart();
  };

  /** 6) Clear entire cart (e.g. after checkout) **/
  const clearCart = async () => {
    await axios.delete('/cart');
    setCart([]);
  };

  // On mount, load products and cart
  useEffect(() => {
    refreshData();
    fetchCart();
  }, []);

  return (
    <AppContext.Provider
      value={{
        data,
        isError,
        cart,
        refreshData,
        addToCart,
        updateQuantity,
        removeFromCart,
        clearCart,
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

export default AppContext;
