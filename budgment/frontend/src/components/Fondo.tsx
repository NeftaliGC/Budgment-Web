import React from 'react';

interface FondoProps {
  children: React.ReactNode; // esto permite meter más componentes dentro
  color?: string; // opcional: puedes personalizar el color
}

const Fondo: React.FC<FondoProps> = ({ children }) => {
  return (
    <div
      style={{
        width: "100vw",
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      {children}
    </div>
  );
};

export default Fondo;